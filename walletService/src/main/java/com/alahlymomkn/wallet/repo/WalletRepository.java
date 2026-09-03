package com.alahlymomkn.wallet.repo;
import com.alahlymomkn.common.enums.WalletType;
import com.alahlymomkn.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserIdAndType(Long userId, WalletType type);
    Optional<Wallet> findByGroupIdAndType(Long groupId, WalletType type);
}