package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "surname", target = "surname")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  User signupRequestToUser(SignupRequest signupRequest);

  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  User signinRequestToUser(SigninRequest signinRequest);
}
