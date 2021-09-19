package customerOrder.fromServer.dao;


import customerOrder.fromServer.model.CompletedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedItemRepository extends JpaRepository<CompletedItem, Long> {
}
