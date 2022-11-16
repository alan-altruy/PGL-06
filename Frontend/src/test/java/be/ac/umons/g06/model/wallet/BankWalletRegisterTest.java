package be.ac.umons.g06.model.wallet;

public class BankWalletRegisterTest extends WalletRegisterTest{

    @Override
    protected WalletRegister getWalletRegister() {
        return new BankWalletRegister();
    }
}
