package hoho.test.auth.provider;

import hoho.test.dto.TokenDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    public static final String AUTHORIZATION = "Authorization";
    private final String secret;
    private final long AccessTokenValidityInMilliseconds;
    private final long RefreshTokenValidityInMilliseconds;

    private Key key;

    public TokenProvider(@Value("${spring.jwt.secret}") String secret,
                         @Value("${spring.jwt.access-token-validity-in-seconds}") long accessTokenValidityInMilliseconds,
                         @Value("${spring.jwt.refresh-token-validity-in-seconds}000") long refreshTokenValidityInMilliseconds){
        this.secret = secret;
        this.AccessTokenValidityInMilliseconds = accessTokenValidityInMilliseconds;
        this.RefreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds;
    }

    //secret값을 BASE64 Decode해서 Key 변수에 할당
    @Override
    public void afterPropertiesSet() throws Exception{
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateToken(Long memberId, String role){
        long now = (new Date()).getTime();
        Date accessValidity = new Date(now + this.AccessTokenValidityInMilliseconds);
        Date refreshValidity = new Date(now + this.RefreshTokenValidityInMilliseconds);
        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .claim("role",role)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessValidity)
                .compact();
        String refreshToken = Jwts.builder()
                .setExpiration(refreshValidity)
                .signWith(key,SignatureAlgorithm.HS512)
                .compact();
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //권한 가져오기
    public Authentication getAuthentication(String token){
        //토큰 복호화
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        //클레임에서 권한에 대한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("role").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        //UserDetails 객체를 만들어서 Authentication 반환
        User principal = new User(claims.getSubject(),"",authorities);
        return new UsernamePasswordAuthenticationToken(principal,token,authorities);
    }

    //토큰 유효 확인
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            log.info("잘못된 JWT 서명입니다");
        }catch (ExpiredJwtException e){
            log.info("만료된 JWT 토큰입니다");
        }catch (UnsupportedJwtException e){
            log.info("지원되지 않는 JWT 토큰입니다");
        }catch (IllegalArgumentException e){
            log.info("JWT 토큰이 잘못되었습니다");
        }
        return false;
    }

    //Request 헤더에서 토큰 꺼내오기
    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader(AUTHORIZATION);
        System.out.println(token);
        if(StringUtils.hasText(token)&&token.startsWith("Bearer")){
            return token.substring(7);
        }
        return null;
    }
}
