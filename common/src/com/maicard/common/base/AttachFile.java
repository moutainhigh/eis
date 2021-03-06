/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maicard.common.base;

import java.io.Serializable;

/**
 * Attach file.
 * 
 * @author Byeongkil Woo
 */
public class AttachFile implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String originFilename;

	private byte[] bytes;

	private long size;

	private boolean checkDelete;

	private boolean checkSave;

	public boolean isCheckSaveOrDelete() {
		return checkSave || checkDelete;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginFilename() {
		return originFilename;
	}

	public void setOriginFilename(String originFilename) {
		this.originFilename = originFilename;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public boolean isCheckDelete() {
		return checkDelete;
	}

	public void setCheckDelete(boolean checkDelete) {
		this.checkDelete = checkDelete;
	}

	public boolean isCheckSave() {
		return checkSave;
	}

	public void setCheckSave(boolean checkSave) {
		this.checkSave = checkSave;
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + Integer.toHexString(hashCode()) + 
			"(" + 
			"name=" + "'" + name + "'" + ", " + 
			"originFilename=" + "'" + originFilename + "'" + ", " + 
			"size=" + "'" + size + "'" + ", " + 
			"checkDelete=" + "'" + checkDelete + "'" + ", " + 
			"checkSave=" + "'" + checkSave + "'" + 
			")";
	}

}
