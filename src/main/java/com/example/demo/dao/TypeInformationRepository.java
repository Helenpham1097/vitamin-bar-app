package com.example.demo.dao;
import com.example.demo.model.TypeInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TypeInformationRepository extends JpaRepository<TypeInformation, Long> {
    @Query(value = "Select * from specificbeverage where specificbeverage.name =:name", nativeQuery = true)
    TypeInformation findSpecificItemByName(@Param("name") String name);

    @Query(value = "Select * from specificbeverage where specificbeverage.item_code =:code", nativeQuery = true)
    TypeInformation findSpecificItemByItsCode(@Param("code") String itemCode);

    @Transactional
//    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Modifying
    @Query(value = "delete from specificbeverage where specificbeverage.name =:name", nativeQuery = true)
    void deleteByName(@Param("name") String name);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query(value = "delete from specificbeverage where specificbeverage.type_id = ?1", nativeQuery = true)
    int deleteByBeverageId(Long id);
}
