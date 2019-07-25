package com.maicard.common.util;

import com.maicard.standard.EisError;

public class CardError {

	public static boolean isCardError(int status){
		if(status  == EisError.cardPasswordError.getId() 
				|| status == EisError.moneyNotEnough.getId()
				|| status == EisError.serialNumberError.getId()
				|| status == EisError.limitedCard.getId()
				|| status == EisError.moneyIsFrozen.getId()
				|| status == EisError.cardUsedBefore.getId()
				|| status == EisError.notActive.getId()){
			return true;
		}
		return false;
	}

}
