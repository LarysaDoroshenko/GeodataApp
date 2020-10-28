package petproject.geodata.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.domain.PlaceEntity;
import petproject.geodata.dto.AddressDto;
import petproject.geodata.dto.PlaceDto;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class ConverterConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        modelMapper.typeMap(AddressEntity.class, AddressDto.class)
                .addMapping(AddressEntity::getCity, AddressDto::setTown);
        modelMapper.typeMap(PlaceEntity.class, PlaceDto.class)
                .addMapping(PlaceEntity::getDisplayName, PlaceDto::setName);
        modelMapper.typeMap(PlaceEntity.class, PlaceDto.class)
                .addMapping(PlaceEntity::getAddressEntity, PlaceDto::setAddressDto);

        modelMapper.typeMap(AddressDto.class, AddressEntity.class)
                .addMapping(AddressDto::getTown, AddressEntity::setCity);
        modelMapper.typeMap(PlaceDto.class, PlaceEntity.class)
                .addMapping(PlaceDto::getName, PlaceEntity::setDisplayName);
        modelMapper.typeMap(PlaceDto.class, PlaceEntity.class)
                .addMapping(PlaceDto::getAddressDto, PlaceEntity::setAddressEntity);

        return modelMapper;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        return objectMapper;
    }

}
