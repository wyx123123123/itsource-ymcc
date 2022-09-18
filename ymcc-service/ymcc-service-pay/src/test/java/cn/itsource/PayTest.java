package cn.itsource;

import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.factory.Factory.Payment;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.kernel.util.ResponseChecker;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayApp.class)
public class PayTest {

    @Test
    public void testPay(){
        // 1. 设置参数（全局只需设置一次）
        Factory.setOptions(getOptions());
        try {
            // 2. 发起API调用 网页支付
            AlipayTradePagePayResponse response = Payment.Page().pay(
                    "ymcc测试支12312付单",//标题
                    "34534545",//订单号
                    "123",//金额
                    "http://127.0.0.1:6002/detail.html?courseId=9"//同步回调地址
            );
            // 3. 处理响应或异常
            if (ResponseChecker.success(response)) {
                System.out.println(response.getBody());
                System.out.println("调用成功");
            }
        } catch (Exception e) {
            System.err.println("调用遭遇异常，原因：" + e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    //设置商家的相关参数
    /**
     * 收款方的参数
     * @return
     */
    private static Config getOptions() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";//openapi.alipaydev.com
        config.signType = "RSA2";
        config.appId = "2021000117619030";
        // 为避免私钥随源码泄露，推荐从文件中读取私钥字符串而不是写入源码中
        config.merchantPrivateKey = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDdlF+1BPQFuxMkY7HdOLtDQ47uvjYwTSaZrBtaNvwAPOhESueQPP/GarjgZY2b8mgXy5aV7HluOAZBm0W+RxHX4LGbtcWMKfJOBplcJ5gf5A6TazTs+6rAA8iMk74EErek9/zCkdOvn248TGTJCBMW7JjiZ3swS82Fbhl4fNbAPp4RHLl81fKeZECFsnF14HUVyMvNPtyic86Xklo0NvPbBQxvh6pE9FFKIaOc/DBoSniw3NBrcVfpRqOreV4c82eyTDYkBie1dv13S0iyBjXwDDIRcuzNBNATMJ6GfUHIbtfgPoDSbJ9vfhIfR0+1Ovt+TH8+DLpa69m38EpgRNt1AgMBAAECggEBAMBENgPaB3BNkt+ZjkWaK4vDPGxXKqgg2eSwXY+bdHE0aIqRwHr9IyflhQRZulXeMoqrQsBW0EteW7dj51GjU41R0Gt/f6oO38zqlvpK9Qp/FlK+zM7x1YkF/2hT7qaUIzwHXxDp/pg+rCWwWCfEVOQoasTR7CfNLsmf7oQJN0LZtXSsITMie6oEgFZzPZEXuuIb8FY+AkhasJ5wTtSkeAoyVHP0Uk5rAlz/ofoX718rRbH69kn0zfnzhg29NDx3gYxPaKSyGOzCxChW1tgkwIvJHH6PA6wb0eO9JkJH3ZsexaBTnoZkavcyqrXHd7FSOFtLEJ7SxiEj/v2uPpxXuMECgYEA7jJXtKTeEEhUw1FZD3ClYqPoK1+Y59ZHAzScVXQA+z/Ljrf2UuPBaE9m9HRdzuKb+UmEjCxlNvhSmEeyxidTLt6WPqg7x78UGM7HOcyhha3PeqQeaZ6of7G3dE8YlDxSGp9jBk+VoiXlDQDoDd6EStW6uHgZ+tEOGWaDp9/fSaUCgYEA7iQUg/1GjF1Fg0S/EWSVyVwk1byss7STt7s+q7mMAnidFHlq96bKK1SVdaHcFq4wmAXETEXyt3nKSnipolHHCS1PGQ1w0/7HZ1tIvhcNwcgUaCBEZlHncKWLN4ReKheg1IpVYIuIbG63Vu5dZVX5Nq+s/7K5XD6FKTt9qLTwgZECgYEAuXD8EykfsTF+vCkC2yBpwae1wr7EPgn2cypsY6LSeMir40qZPOMP2fOnRb2qoryNjcCMrsyJaAhf1SpMpjRLcHveIJ29HL9IGCAzvm3vejFdDC/ldXkN2Yw0NRq3GgD0MsHPyEQBTKGP91WzU8je2ZwXqDHol/pVrbS/3txN6/kCgYBq9+33M8eCuUrGIhUpPONDZY5uK6fvuMc5mnQWMuw/F+qHDdsMjvD08bOI+UGhNnFBeCWqlSjnXV+OgBDChrU+3AlRmYX609DCDnDoYfad+r+f3g6bX2L0aIfWUjDx+1cU4WZkTYjkG/9inwN4x1uAQv0UhjVYyASMdtqE83Gs4QKBgQCgAFr9U0Rr6hkF7arnPF+l9AC7H9ruYVVnA0O+NrIbMAf9zOb7lORbS72E757VUesRXtubSaQw/Vk50hxq04KBdRw0bum95wNxZK53WBoZ4hH6cDogSmRHsYPcK6JUpTgOylg7QffqD0INWXLu1ggXQ5XQ6eiK41xL8rHa/CGhUg==";
        //注：如果采用非证书模式，则无需赋值上面的三个证书路径，改为赋值如下的支付宝公钥字符串即可
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgLpiroIjQl508mK/EBk/XmO7XnlXY21KP7QvCDOdCyW1KRVMCFEJIQRrDyKbnKrHWjfnn3XjzB3gLS1utxD7c9bEWnDBKN7eGywpZLMSBysQYFI28G+F55unw+wYVnKzrCtiw0iIIbeTS50/Ze6NGSFqgiGyfaVexDck6dpZJbW+fThH8xEgGYMMeAXHomf7D4ieE6foHsIwc+Od4n2CMeJe7XZKHdfPDjn6dhBVIzBsS3suVUSq6pZhO30w4tZkqclIuTSWZKsfOV6j5DJAjyqyihbaHq9UXQ8o9IFbakUWfUIgZ9/6vaff0KcPGUWYFIBBpnbh2JsY5JMwtGNUhQIDAQAB";
        //可设置异步通知接收服务地址（可选）
        config.notifyUrl = "https://www.baidu.com/callback";// 后台的支付服务的controller接口
        return config;
    }
}
