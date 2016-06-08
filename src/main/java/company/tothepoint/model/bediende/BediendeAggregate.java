package company.tothepoint.model.bediende;

import company.tothepoint.model.contract.Contract;

import java.util.List;

public class BediendeAggregate {
    private Bediende bediende;
    private List<Contract> contracten;

    public BediendeAggregate() {
    }

    public BediendeAggregate(Bediende bediende, List<Contract> contracten) {
        this.bediende = bediende;
        this.contracten = contracten;
    }

    public Bediende getBediende() {
        return bediende;
    }

    public void setBediende(Bediende bediende) {
        this.bediende = bediende;
    }

    public List<Contract> getContracten() {
        return contracten;
    }

    public void setContracten(List<Contract> contracten) {
        this.contracten = contracten;
    }
}
