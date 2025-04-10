package edu.ntnu.idatt2105.backend.service;

import edu.ntnu.idatt2105.backend.dto.token.SigninRequest;
import edu.ntnu.idatt2105.backend.dto.token.SignupRequest;
import edu.ntnu.idatt2105.backend.dto.user.GetUserInfoResponse;
import edu.ntnu.idatt2105.backend.dto.user.UpdateUserInfoRequest;
import edu.ntnu.idatt2105.backend.mapper.UserMapper;
import edu.ntnu.idatt2105.backend.model.User;
import edu.ntnu.idatt2105.backend.repository.BidRepository;
import edu.ntnu.idatt2105.backend.repository.BookmarkRepository;
import edu.ntnu.idatt2105.backend.repository.BrowseHistoryRepository;
import edu.ntnu.idatt2105.backend.repository.ItemRepository;
import edu.ntnu.idatt2105.backend.repository.PurchaseRepository;
import edu.ntnu.idatt2105.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service class for the User model.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final ItemRepository itemRepository;

  private final BookmarkRepository bookmarkRepository;

  private final BrowseHistoryRepository browseHistoryRepository;

  private final BidRepository bidRepository;

  private final PurchaseRepository purchaseRepository;

  private final PasswordEncoder passwordEncoder;

  /**
   * gets the user information from the users email.
   *
   * @param email the email of the user
   * @return the user object
   */
  public User getUserByEmail(String email) {
    return userRepository.getUserByEmail(email);
  }

  /**
   * checks if the passed password matches the stored password.
   *
   * @param signinRequest the signin request
   * @return do passwords match return boolean
   */
  public boolean checkCredentials(SigninRequest signinRequest) {
    String userPassword = userRepository.getPasswordByEmail(signinRequest.getEmail());
    return passwordEncoder.matches(signinRequest.getPassword(), userPassword);
  }

  /**
   * create user based on a signup request.
   *
   * @param signupRequest the signup request
   */
  public void createUser(SignupRequest signupRequest) {
    User user = UserMapper.INSTANCE.signupRequestToUser(signupRequest);
    user.setRole("ROLE_USER");
    user.setPassword(encodePassword(user.getPassword()));
    userRepository.createUser(user);
  }

  //  public void createAdmin(User user) {
  //    user.setRole("ROLE_ADMIN");
  //    user.setPassword(encodePassword(user.getPassword()));
  //    userRepository.createUser(user);
  //  }

  /**
   * get info of logged in user.
   *
   * @param userID logged in user's id
   * @return the user info response
   */
  public GetUserInfoResponse getUser(int userID) {
    User user = userRepository.getUser(userID);
    return UserMapper.INSTANCE.userToGetUserInfoResponse(user);
  }

  /**
   * edit info of logged-in user.
   *
   * @param updateUserInfoRequest the edit info request
   */
  public void editUser(UpdateUserInfoRequest updateUserInfoRequest) {
    User user = UserMapper.INSTANCE.updateUserInfoRequestToUser(updateUserInfoRequest);
    String userID = SecurityContextHolder.getContext().getAuthentication().getName();
    user.setUserID(Integer.parseInt(userID));
    userRepository.updateUser(user);
  }















  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }
}
