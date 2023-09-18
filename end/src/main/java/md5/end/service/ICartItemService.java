package md5.end.service;

import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.CartItemRequest;
import md5.end.model.dto.response.CartItemResponse;


public interface ICartItemService extends IGenericService<CartItemRequest, CartItemResponse> {
    CartItemResponse findByProductId(Long id) throws NotFoundException;
    void checkout();
}
