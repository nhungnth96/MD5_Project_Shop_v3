package md5.end.service.impl;

import md5.end.exception.BadRequestException;
import md5.end.exception.NotFoundException;
import md5.end.model.dto.request.OrderRequest;
import md5.end.model.dto.response.OrderResponse;
import md5.end.model.entity.order.Order;
import md5.end.model.entity.order.OrderStatus;
import md5.end.model.entity.product.Product;
import md5.end.repository.IOrderRepository;
import md5.end.service.ICartItemService;
import md5.end.service.IOrderService;
import md5.end.service.amapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {
    @Autowired
    private ICartItemService cartItemService;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public List<OrderResponse> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> orderMapper.getResponseFromEntity(order))
                .collect(Collectors.toList());
    }
    @Override
    public OrderResponse findByUserId(Long userId) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findByUserId(userId);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order of user's id "+userId+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse findByStatus(OrderStatus orderStatus) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findByStatus(orderStatus);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Status "+orderStatus+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse findByOrderDate(String orderDate) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findByOrderDate(orderDate);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order of date "+orderDate+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse findById(Long id) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(!orderOptional.isPresent()){
            throw new NotFoundException("Order's id "+id+" not found.");
        }
        return orderMapper.getResponseFromEntity(orderOptional.get());
    }

    @Override
    public OrderResponse save(OrderRequest orderRequest) throws BadRequestException {
        Order order = orderRepository.save(orderMapper.getEntityFromRequest(orderRequest));
        return orderMapper.getResponseFromEntity(order);
    }
    @Override
    public OrderResponse updateStatus(OrderRequest orderRequest, Long orderId, OrderStatus orderStatus) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException("Order's id "+orderId+" not found.");
        }
        Order order = orderMapper.getEntityFromRequest(orderRequest);
        order.setId(orderId);
        order.setStatus(orderStatus);
        return orderMapper.getResponseFromEntity(orderRepository.save(order));
    }
    @Override
    public OrderResponse cancel(Long id) throws NotFoundException {
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (!orderOptional.isPresent()) {
            throw new NotFoundException("Order's id "+id+" not found.");
        }
        orderOptional.get().setActive(false);
        orderOptional.get().setStatus(OrderStatus.CANCEL);
        return orderMapper.getResponseFromEntity(orderRepository.save(orderOptional.get()));
    }



    @Override
    public OrderResponse update(OrderRequest orderRequest, Long id) throws NotFoundException {
        return null;
    }

    @Override
    public OrderResponse deleteById(Long id) throws NotFoundException {
        return null;
    }
}
