package md5.end.repository;

import md5.end.model.entity.order.CartItem;
import md5.end.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository <CartItem,Long>{
    List<CartItem> findCartItemByUser(User user);
    Optional<CartItem> findByProductId(Long id);
}
