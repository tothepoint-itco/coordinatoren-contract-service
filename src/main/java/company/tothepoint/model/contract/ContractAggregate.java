package company.tothepoint.model.contract;

import company.tothepoint.model.bediende.Bediende;

public class ContractAggregate {
    private Bediende bediende;
    private Contract contract;

    public ContractAggregate() {
    }

    public ContractAggregate(Contract contract, Bediende bediende) {
        this.bediende = bediende;
        this.contract = contract;
    }

    public Bediende getBediende() {
        return bediende;
    }

    public void setBediende(Bediende bediende) {
        this.bediende = bediende;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
