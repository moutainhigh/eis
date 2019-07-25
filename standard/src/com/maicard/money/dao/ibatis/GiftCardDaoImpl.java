package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.GiftCardCriteria;
import com.maicard.money.dao.GiftCardDao;
import com.maicard.money.domain.GiftCard;

public class GiftCardDaoImpl extends BaseDao implements GiftCardDao {

	@Override
	public int insert(GiftCard giftCard) throws DataAccessException {
		return getSqlSessionTemplate().insert("GiftCard.insert", giftCard);
	}

	public int update(GiftCard giftCard) throws DataAccessException {
		return getSqlSessionTemplate().update("GiftCard.update", giftCard);
	}


	public int delete(String cardNumber) throws DataAccessException {
		return getSqlSessionTemplate().delete("GiftCard.delete", cardNumber);
	}


	public List<GiftCard> list(GiftCardCriteria giftCardCriteria) throws DataAccessException {
		Assert.notNull(giftCardCriteria, "giftCardCriteria must not be null");

		return getSqlSessionTemplate().selectList("GiftCard.list", giftCardCriteria);
	}

	public List<GiftCard> listOnPage(GiftCardCriteria giftCardCriteria) throws DataAccessException {
		Assert.notNull(giftCardCriteria, "giftCardCriteria must not be null");
		Assert.notNull(giftCardCriteria.getPaging(), "paging must not be null");

		int totalResults = count(giftCardCriteria);
		Paging paging = giftCardCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("GiftCard.list", giftCardCriteria, rowBounds);
	}

	public int count(GiftCardCriteria giftCardCriteria) throws DataAccessException {
		Assert.notNull(giftCardCriteria, "giftCardCriteria must not be null");

		return ((Integer) getSqlSessionTemplate().selectOne("GiftCard.count", giftCardCriteria)).intValue();
	}



	@Override
	public GiftCard fetchWithLock(GiftCardCriteria giftCardCriteria) {
		String objectIds = null;
		if(giftCardCriteria.getObjectIds() != null && giftCardCriteria.getObjectIds().length > 1){
			objectIds = "";
			for(int objectId : giftCardCriteria.getObjectIds()){
				objectIds += objectId + ",";
			}
		}
		logger.info("尝试锁定礼品卡[对象类型=" + giftCardCriteria.getObjectType() );
		logger.info("对象ID范围:" + objectIds + "对象扩展ID:" + giftCardCriteria.getObjectExtraId());
		logger.info("锁定ID=" + giftCardCriteria.getLockGlobalUniqueId()); 
		logger.info("锁定一个状态为="+ giftCardCriteria.getCurrentStatus()[0] );
		logger.info("并将其状态改为: "+ giftCardCriteria.getNewStatus() + "].");
		if (giftCardCriteria.getCardNumber().equals("")){
			try{
				if(getSqlSessionTemplate().update("GiftCard.fetchWithLock", giftCardCriteria) < 1){
					logger.warn("未能锁定礼品卡，返回数据<1");
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.warn("未能锁定礼品卡");
				return null;
			}
		}
		else{
			try{
				if(getSqlSessionTemplate().update("GiftCard.fetchWithLocksimple", giftCardCriteria) < 1){
					logger.warn("未能锁定礼品卡，返回数据<1");
					return null;
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.warn("未能锁定礼品卡");
				return null;
			}	
		}
		giftCardCriteria.setCurrentStatus(giftCardCriteria.getNewStatus());
		List<GiftCard> giftCardList = list(giftCardCriteria);
		if(giftCardList == null || giftCardList.size() != 1){
			logger.warn("未找到锁定ID[" + giftCardCriteria.getLockGlobalUniqueId() + "]的礼品卡");		
			return null;
		}
		logger.info("成功找到锁定ID[" + giftCardCriteria.getLockGlobalUniqueId() + "]的礼品卡");		
		return giftCardList.get(0);
	}

	@Override
	public GiftCard select(int giftCardId) {
		return  getSqlSessionTemplate().selectOne("GiftCard.select", giftCardId);
	}

}
