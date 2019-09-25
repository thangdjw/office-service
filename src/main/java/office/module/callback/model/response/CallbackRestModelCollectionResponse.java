package office.module.callback.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import office.module.callback.model.entity.CallbackRestModel;

import java.util.Collection;

@Getter @Setter
@AllArgsConstructor
public class CallbackRestModelCollectionResponse {
    private Collection<CallbackRestModel> callbackModel;
}
