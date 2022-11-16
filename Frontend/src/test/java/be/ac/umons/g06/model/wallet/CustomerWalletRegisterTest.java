package be.ac.umons.g06.model.wallet;

public class CustomerWalletRegisterTest extends WalletRegisterTest{

    @Override
    protected WalletRegister getWalletRegister() {
        return new CustomerWalletRegister();
    }
}
