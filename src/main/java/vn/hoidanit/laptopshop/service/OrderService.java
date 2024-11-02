package vn.hoidanit.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.laptopshop.domain.Order;
import vn.hoidanit.laptopshop.repository.OderRepository;

@Service
public class OrderService {

    private final OderRepository orderRepository;

    public OrderService ( OderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public List<Order> fetchAllOrders(){
         return this.orderRepository.findAll();
    }
    
}
