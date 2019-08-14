package com.jt.service;

import java.util.List;

import com.jt.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartListByUser(Long userId);

	void saveCart(Cart cart);

	void updateCartNum(Cart cart);

}
