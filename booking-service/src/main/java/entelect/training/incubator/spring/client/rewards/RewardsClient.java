package entelect.training.incubator.spring.client.rewards;

import entelect.training.incubator.spring.client.gen.CaptureRewardsRequest;
import entelect.training.incubator.spring.client.gen.CaptureRewardsResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import java.math.BigDecimal;

public class RewardsClient extends WebServiceGatewaySupport {
    public CaptureRewardsResponse captureRewards(String passport, BigDecimal amount) {
        CaptureRewardsRequest request = new CaptureRewardsRequest();
        request.setPassportNumber(passport);
        request.setAmount(amount);

        CaptureRewardsResponse response = (CaptureRewardsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }
}
