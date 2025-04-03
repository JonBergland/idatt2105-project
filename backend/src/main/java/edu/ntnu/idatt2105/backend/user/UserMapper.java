package edu.ntnu.idatt2105.backend.user;

import edu.ntnu.idatt2105.backend.security.dto.SigninRequest;
import edu.ntnu.idatt2105.backend.security.dto.SignupRequest;
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
  User signupRequestToUser(SignupRequest signupRequest);

  /**
   * Maps signinRequest dto to user model.
   *
   * @param signinRequest the dto to map
   * @return the mapped user model
   */
  User signinRequestToUser(SigninRequest signinRequest);
}
