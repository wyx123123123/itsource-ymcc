package cn.itsource.service;

public interface ITokenService {

    /**
     * 创建防止重复token
     * @param courseId
     * @return
     */
    String createToken(String courseId);
}
