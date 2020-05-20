package q.app.q006.beans;

import q.app.q006.helpers.AppConstants;
import q.app.q006.model.vehicle.PublicMake;
import q.app.q006.model.vehicle.PublicModel;
import q.app.q006.reqs.NotLoggedRequester;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@SessionScoped
public class MakesBean implements Serializable {

    private List<PublicMake> makes;

    @Inject
    private NotLoggedRequester reqs;

    @PostConstruct
    private void init(){
        makes = new ArrayList<>();
        Response r = reqs.getSecuredRequest(AppConstants.GET_ACTIVE_MAKES);
        if (r.getStatus() == 200) {
            makes = r.readEntity(new GenericType<List<PublicMake>>() {
            });
        }
    }

    public List<PublicModel> getModelsFromMakeId(int id){
        for(var make : makes){
            if(make.getId() == id){
                return make.getModels();
            }
        }
        return null;
    }

    public List<PublicMake> getMakes() {
        return makes;
    }

    public void setMakes(List<PublicMake> makes) {
        this.makes = makes;
    }
}
