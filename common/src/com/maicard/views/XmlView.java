package com.maicard.views;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.BeansException;
import org.springframework.oxm.Marshaller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;

/**
 * Spring-MVC {@link View} that allows for response context to be rendered as the result of marshalling by a {@link
 * Marshaller}.
 *
 * <p>The Object to be marshalled is supplied as a parameter in the model and then {@linkplain
 * #locateToBeMarshalled(Map) detected} during response rendering. Users can either specify a specific entry in the
 * model via the {@link #setModelKey(String) sourceKey} property or have Spring locate the Source object.
 *
 * @author Arjen Poutsma
 * @since 3.0
 */
public class XmlView extends AbstractView {

	private static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";

	/**
	 * Default content type. Overridable as bean property.
	 */
	public static final String DEFAULT_CONTENT_TYPE = "application/xml; charset=utf-8";

	private Marshaller marshaller;

	private String modelKey;

	/**
	 * Constructs a new {@code MarshallingView} with no {@link Marshaller} set. The marshaller must be set after
	 * construction by invoking {@link #setMarshaller(Marshaller)}.
	 */
	public XmlView() {
		setContentType(DEFAULT_CONTENT_TYPE);
		setExposePathVariables(false);
	}

	/**
	 * Constructs a new {@code MarshallingView} with the given {@link Marshaller} set.
	 */
	public XmlView(Marshaller marshaller) {
		Assert.notNull(marshaller, "'marshaller' must not be null");
		setContentType(DEFAULT_CONTENT_TYPE);
		this.marshaller = marshaller;
		setExposePathVariables(false);
	}

	/**
	 * Sets the {@link Marshaller} to be used by this view.
	 */
	public void setMarshaller(Marshaller marshaller) {
		Assert.notNull(marshaller, "'marshaller' must not be null");
		this.marshaller = marshaller;
	}

	/**
	 * Set the name of the model key that represents the object to be marshalled. If not specified, the model map will be
	 * searched for a supported value type.
	 *
	 * @see Marshaller#supports(Class)
	 */
	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}

	@Override
	protected void initApplicationContext() throws BeansException {
		Assert.notNull(marshaller, "Property 'marshaller' is required");
	}

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType(DEFAULT_CONTENT_TYPE);
		response.setHeader("Cache-Control",	"no-store, max-age=0, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		
		//List<Object> toBeMarshalled = locateToBeMarshalled2(model);
		Map<String, Object> toBeMarshalled = locateToBeMarshalled(model);
		if (toBeMarshalled == null) {
			throw new ServletException("Unable to locate object to be marshalled in model: " + model);
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
		marshaller.marshal(toBeMarshalled, new StreamResult(bos));
		StringBuffer data = new StringBuffer();
		data.append(xmlHeader);
		data.append(new String(bos.toByteArray(),"UTF-8"));
		response.getWriter().write(data.toString().replaceAll("linked-hash-map", "eis"));
		response.getWriter().flush();
		response.getWriter().close();
		data = null;
		//FileCopyUtils.copy(bos.toByteArray(), response.getOutputStream());
	}

	/**
	 * Locates the object to be marshalled. The default implementation first attempts to look under the configured
	 * {@linkplain #setModelKey(String) model key}, if any, before attempting to locate an object of {@linkplain
	 * Marshaller#supports(Class) supported type}.
	 *
	 * @param model the model Map
	 * @return the Object to be marshalled (or {@code null} if none found)
	 * @throws ServletException if the model object specified by the {@linkplain #setModelKey(String) model key} is not
	 *                          supported by the marshaller
	 * @see #setModelKey(String)
	 */
	protected Map<String, Object> locateToBeMarshalled(Map<String, Object> model) throws ServletException {
		Map<String, Object> validMarshall = new LinkedHashMap<String, Object>();
		if (this.modelKey != null) {
			Object o = model.get(this.modelKey);
			if (o == null) {
				throw new ServletException("Model contains no object with key [" + modelKey + "]");
			}
			if (!this.marshaller.supports(o.getClass())) {
				throw new ServletException("Model object [" + o + "] retrieved via key [" + modelKey +
						"] is not supported by the Marshaller");
			}
			validMarshall.put(this.modelKey, o);
		}
		for (String key : model.keySet()) {
			Object o = model.get(key);
			if(o == null){
				continue;
			}
			if(o instanceof BindingResult){
				continue;
			}
			if ( this.marshaller.supports(o.getClass())) {
				validMarshall.put(key, o);
			}
		}
		return validMarshall;
	}

	protected List<Object> locateToBeMarshalled2(Map<String, Object> model) throws ServletException {
		List<Object> validMarshall = new ArrayList<Object>();
		if (this.modelKey != null) {
			Object o = model.get(this.modelKey);
			if (o == null) {
				throw new ServletException("Model contains no object with key [" + modelKey + "]");
			}
			if (!this.marshaller.supports(o.getClass())) {
				throw new ServletException("Model object [" + o + "] retrieved via key [" + modelKey +
						"] is not supported by the Marshaller");
			}
			validMarshall.add(o);
		}
		for (String key : model.keySet()) {
			Object o = model.get(key);
			if(o == null){
				continue;
			}
			if(o instanceof BindingResult){
				continue;
			}
			if ( this.marshaller.supports(o.getClass())) {
				validMarshall.add(o);
			}
		}
		return validMarshall;
	}

}
