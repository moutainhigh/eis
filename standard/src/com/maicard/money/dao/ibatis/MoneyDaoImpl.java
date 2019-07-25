package com.maicard.money.dao.ibatis;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.common.base.BaseDao;
import com.maicard.common.util.Paging;
import com.maicard.money.criteria.MoneyCriteria;
import com.maicard.money.dao.MoneyDao;
import com.maicard.money.domain.Money;

@Repository
public class MoneyDaoImpl extends BaseDao implements MoneyDao {


	public void insert(Money money) throws DataAccessException {
		getSqlSessionTemplate().insert("com.maicard.money.sql.Money.insert", money);
	}

	public int update(Money money) throws DataAccessException {
		return getSqlSessionTemplate().update("com.maicard.money.sql.Money.update", money);
	}

	public int delete(long uuid) throws DataAccessException {
		return getSqlSessionTemplate().delete("com.maicard.money.sql.Money.delete", (uuid));
	}

	public Money select(long uuid) throws DataAccessException {
		try{
			return (Money) getSqlSessionTemplate().selectOne("com.maicard.money.sql.Money.select", (uuid));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
   public void charge(Money money) throws DataAccessException{
	  getSqlSessionTemplate().selectOne("com.maicard.money.sql.Money.chargeMoney", money); 
   }
	public List<Money> list(MoneyCriteria moneyCriteria) throws DataAccessException {
		Assert.notNull(moneyCriteria, "moneyCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Money.list", moneyCriteria);
	}
	public List<Money> listByPartner() throws DataAccessException {
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Money.listByPartner");
	}
	public List<Money> listOnPage(MoneyCriteria moneyCriteria) throws DataAccessException {
		Assert.notNull(moneyCriteria, "moneyCriteria must not be null");
		Assert.notNull(moneyCriteria.getPaging(), "paging must not be null");

		int totalResults = count(moneyCriteria);
		Paging paging = moneyCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Money.list", moneyCriteria,  rowBounds);
	}

	public int count(MoneyCriteria moneyCriteria) throws DataAccessException {
		Assert.notNull(moneyCriteria, "moneyCriteria must not be null");
		return ((Integer) getSqlSessionTemplate().selectOne("com.maicard.money.sql.Money.count", moneyCriteria)).intValue();
	}

	@Override
	public List<Long> listPk(MoneyCriteria moneyCriteria) {

		Assert.notNull(moneyCriteria, "moneyCriteria must not be null");

		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Money.listPk", moneyCriteria);
	}

	@Override
	public List<Long> listPkOnPage(MoneyCriteria moneyCriteria) {
		Assert.notNull(moneyCriteria, "moneyCriteria must not be null");
		Assert.notNull(moneyCriteria.getPaging(), "paging must not be null");

		int totalResults = count(moneyCriteria);
		Paging paging = moneyCriteria.getPaging();
		paging.setTotalResults(totalResults);
		RowBounds rowBounds = new RowBounds(paging.getFirstResult(), paging.getMaxResults());
		return getSqlSessionTemplate().selectList("com.maicard.money.sql.Money.listPk", moneyCriteria,  rowBounds);
	}

	/*public int plus(Money money) throws DataAccessException {
		Money _oldMoney = select(money.getUuid());
		if(_oldMoney  == null){//插入新数据
			logger.info("没有已存在的用户资金关联记录,插入新记录");
			insert(money);
			Money _oldMoney2 = select(money.getUuid());
			if(_oldMoney2 != null){
				return 1;
			} else {
				logger.error("无法增加新的资金关联");
				return 0;
			}

		}
		Money _newMoney = select(money.getUuid());
		if(_newMoney == null){
			return 0;
		}
		return getSqlSessionTemplate().update("Money.plus", money);

	}

	@Override
	public int minus(Money money) throws DataAccessException {
		if(money == null){
			throw new RequiredObjectIsNullException("扣除资金的money对象为空");
		}
		logger.debug("尝试扣除账户[" + money.getUuid() + "]的资金[" + money +"]");
		Money _oldMoney = select(money.getUuid());
		if(_oldMoney  == null){
			logger.error("资金帐户[" + money.getUuid() + "]不存在");
			throw new ObjectNotFoundByIdException("资金帐户[" + money.getUuid() + "]不存在");
		}
		return getSqlSessionTemplate().update("Money.minus", money);
	}

	@Override
	@Transactional
	public int lock(Money money) {
		int rs = getSqlSessionTemplate().update("Money.lock", money);
		logger.info("锁定资金结果:" + rs);
		if(rs == 1){
			//XXX 不能直接使用money = select(???)，这样会生成新的对象，导致最新inComingMoney值并没有赋予传入的那个money
			Money _newMoney = select(money.getUuid());
			money.setIncomingMoney(_newMoney.getIncomingMoney());
			money.setChargeMoney(_newMoney.getChargeMoney());
			money.setFrozenMoney(_newMoney.getFrozenMoney());
			money.setTransitMoney(_newMoney.getTransitMoney());
			money.setCoin(_newMoney.getCoin());
			money.setPoint(_newMoney.getPoint());
			//logger.info("锁定资金完成，取出最新数据作为返回对象:" + money + "=>" + money.getIncomingMoney());
			_newMoney = null;
	}
		return rs;
	}

	@Override
	public int unLock(Money money) {
		return getSqlSessionTemplate().update("Money.unLock", money);
	}*/

}
