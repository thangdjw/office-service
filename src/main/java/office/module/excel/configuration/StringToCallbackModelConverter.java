package office.module.excel.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import office.module.callback.model.entity.CallbackRestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToCallbackModelConverter implements Converter<String, CallbackRestModel> {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public CallbackRestModel convert(String source) {
        return objectMapper.convertValue(source, CallbackRestModel.class);
    }
}
