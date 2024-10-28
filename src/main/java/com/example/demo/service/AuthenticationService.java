package com.example.demo.service;

import com.example.demo.dto.request.AuthenticationRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.reponse.AuthenticationResponse;
import com.example.demo.reponse.IntrospectResponse;
import com.example.demo.reponsitory.UserReponsitory;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserReponsitory userReponsitory;

    @NonFinal
    @Value("${signAuthKey}")  //lấy giá trị signAuthKey từ file application.properties truyền vô bên dưới
    protected String SignIn_key;
            //="qTDBpDVGHSTPDsbGBVMyLEs8s7xCVlYBYAxP0hFWCZx3Wzlb+dQwRCJe+7h/GXSj";  //key để đọc và giải mã, quan trọng

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        JWSVerifier verifier = new MACVerifier(SignIn_key.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        signedJWT.verify(verifier);
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime(); //check thời gian quá hạn chưa
        var verified = signedJWT.verify(verifier);  //check đúng token chưa

        return IntrospectResponse.builder()
                .valid(verified && expirationTime.after(new Date()))
                .build();  //trả về valid ở IntrospectResponse


    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userReponsitory.findByUsername(request.getUsername()).orElseThrow(() ->
                new AppException(ErrorCode.USER_NOT_FOUND));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticate =  passwordEncoder.matches(request.getPassword(), user.getPassword()); // phương thức check pass nhập vào và pass tìm từ username

        if(!authenticate) throw new AppException(ErrorCode.UNAUTHENTICATION);

        var token = generateToken(user); //tạo token từ user

        return AuthenticationResponse.builder()
                .token(token)
                .userInfor(user)
                .authenticated(true)
                .build();


    }

    private String generateToken(User user) { //token gồm header, payload,
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("minhduydeptraivaichuong.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(4, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SignIn_key.getBytes()));  //kí và giải mã token
            return jwsObject.serialize();  //gen ra 1 token theo kiểu có bao gồm hearder bà payload như trên
        } catch (JOSEException e) {
            System.out.println("Cant generate token");
            throw new RuntimeException(e);
        }
    }

    private String buildScope (User user) {  // Hàm lấy user role từ User
        StringJoiner scopes = new StringJoiner(" ");

        if(!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(scopes::add);

        return scopes.toString();
    }
}
