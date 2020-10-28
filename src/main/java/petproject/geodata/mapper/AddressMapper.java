package petproject.geodata.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import petproject.geodata.domain.AddressEntity;
import petproject.geodata.dto.AddressDto;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressMapper {

    private final ModelMapper modelMapper;

    public AddressEntity toEntity(AddressDto addressDto) {
        return Objects.isNull(addressDto) ? null : modelMapper.map(addressDto, AddressEntity.class);
    }

    public AddressDto toDto(AddressEntity addressEntity) {
        return Objects.isNull(addressEntity) ? null : modelMapper.map(addressEntity, AddressDto.class);
    }

}
