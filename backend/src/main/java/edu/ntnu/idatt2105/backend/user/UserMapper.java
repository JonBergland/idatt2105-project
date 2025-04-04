package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
import edu.ntnu.idatt2105.backend.user.dto.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.user.dto.UpdateUserInfoRequest;
import edu.ntnu.idatt2105.backend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * A Mapper for mapping between User and DTOs.
 */
@Mapper
public interface UserMapper {
  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  /**
   * Maps signupRequest dto to user model.
   *
   * @param signupRequest the dto to map
   * @return the mapped user model
   */
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "surname", target = "surname")
  @Mapping(source = "countryCode", target = "countryCode")
  User signupRequestToUser(SignupRequest signupRequest);

  /**
   * Maps signinRequest dto to user model.
   *
   * @param signinRequest the dto to map
   * @return the mapped user model
   */
  @Mapping(source = "email", target = "email")
  @Mapping(source = "password", target = "password")
  User signinRequestToUser(SigninRequest signinRequest);

  @Mapping(source = "userID", target = "userID")
  @Mapping(source = "email", target = "email")
  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "countryCode", target = "countryCode")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "surname", target = "surname")
  @Mapping(source = "role", target = "role")
  @Mapping(source = "latitude", target = "latitude")
  @Mapping(source = "longitude", target = "longitude")
  @Mapping(source = "city", target = "city")
  @Mapping(source = "postalCode", target = "postalCode")
  @Mapping(source = "address", target = "address")
  GetUserInfoResponse userToGetUserInforesponse(User user);

  @Mapping(source = "phoneNumber", target = "phoneNumber")
  @Mapping(source = "countryCode", target = "countryCode")
  @Mapping(source = "name", target = "name")
  @Mapping(source = "surname", target = "surname")
  @Mapping(source = "latitude", target = "latitude")
  @Mapping(source = "longitude", target = "longitude")
  @Mapping(source = "city", target = "city")
  @Mapping(source = "postalCode", target = "postalCode")
  @Mapping(source = "address", target = "address")
  User updateUserInfoRequestToUser(UpdateUserInfoRequest updateUserInfoRequest);
}
