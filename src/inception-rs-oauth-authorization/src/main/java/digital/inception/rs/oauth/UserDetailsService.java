package digital.inception.rs.oauth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService
{
  @Override
  public UserDetails loadUserByUsername(
    String s)
    throws UsernameNotFoundException
  {
    return null;
  }
}
