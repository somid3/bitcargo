package views.forms;

import play.data.validation.Constraints;

public class CargoUpdateEmailForm {

    @Constraints.Required
    public Long cargoId = 0L;

    @Constraints.Required
    public String cargoManageUri = "";

    @Constraints.Email
    @Constraints.Required
    public String newEmail = "";

    public CargoUpdateEmailForm() {
    }

    public CargoUpdateEmailForm(Long cargoId, String cargoManageUri, String newEmail) {
        this.cargoId = cargoId;
        this.cargoManageUri = cargoManageUri;
        this.newEmail = newEmail;
    }
}
