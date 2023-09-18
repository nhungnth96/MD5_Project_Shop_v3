package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.CartItemRequest;
import md5.end.model.dto.response.CartItemResponse;
import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.order.Order;
import md5.end.model.entity.product.Brand;
import md5.end.model.entity.product.Product;
import md5.end.repository.IBrandRepository;
import md5.end.repository.ICartItemRepository;
import md5.end.repository.IProductRepository;
import md5.end.service.ICartItemService;
import md5.end.service.amapper.BrandMapper;
import md5.end.service.amapper.CartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private ICartItemRepository cartItemRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private CartItemMapper cartItemMapper;
    @Override
    public List<CartItemResponse> findAll() {
        List<CartItem> cartItems = cartItemRepository.findAll();
        return cartItems.stream()
                .map(cartItem -> cartItemMapper.getResponseFromEntity(cartItem))
                .collect(Collectors.toList());
    }

    @Override
    public CartItemResponse findById(Long id) throws NotFoundException {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if(!cartItemOptional.isPresent()){
            throw new NotFoundException("Cart item's id "+id+" not found.");

        }
        return cartItemMapper.getResponseFromEntity(cartItemOptional.get());
    }

    @Override
    public CartItemResponse findByProductId(Long id) throws NotFoundException {

        Optional<CartItem> cartItemOptional = cartItemRepository.findByProductId(id);
        if(!cartItemOptional.isPresent()){
            throw new NotFoundException("Product's id "+id+" not found.");
        }
        return cartItemMapper.getResponseFromEntity(cartItemOptional.get());
    }

    @Override
    public CartItemResponse save(CartItemRequest cartItemRequest) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(cartItemRequest.getProductId());
        if(!productOptional.isPresent()){
            throw new NotFoundException("Product's id "+cartItemRequest.getProductId()+" not found.");
        }
        Optional<CartItem> cartItemOptional = cartItemRepository.findByProductId((cartItemRequest.getProductId()));
        if(cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(cartItem.getQuantity()+cartItemRequest.getQuantity());
            return cartItemMapper.getResponseFromEntity(cartItemRepository.save(cartItem));
        }
        CartItem cartItem = cartItemRepository.save(cartItemMapper.getEntityFromRequest(cartItemRequest));
        return cartItemMapper.getResponseFromEntity(cartItem);
}

    @Override
    public CartItemResponse update(CartItemRequest cartItemRequest, Long id) throws NotFoundException {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if (!cartItemOptional.isPresent()) {
            throw new NotFoundException("Cart item's id "+id+" not found.");
        }
        cartItemRequest.setProductId(cartItemOptional.get().getProduct().getId());
        CartItem cartItem = cartItemMapper.getEntityFromRequest(cartItemRequest);
        cartItem.setId(id);
        return cartItemMapper.getResponseFromEntity(cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponse deleteById(Long id) throws NotFoundException {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(id);
        if(!cartItemOptional.isPresent()){
            throw new NotFoundException("Cart item's id "+id+" not found.");
        }
        cartItemRepository.deleteById(id);
        return cartItemMapper.getResponseFromEntity(cartItemOptional.get());
    }

    public void checkout(){
        List<CartItem> cartItems = cartItemRepository.findAll();
        cartItems.clear();
    }
}
