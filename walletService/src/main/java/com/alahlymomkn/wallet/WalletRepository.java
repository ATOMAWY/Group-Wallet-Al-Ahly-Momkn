package com.alahlymomkn.wallet;
import com.alahlymomkn.common.WalletType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserIdAndType(Long userId, WalletType type);
    Optional<Wallet> findByGroupIdAndType(Long groupId, WalletType type);
}