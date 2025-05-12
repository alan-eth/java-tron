package org.tron.core.services.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tron.core.Wallet;
import org.tron.protos.contract.BalanceContract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Slf4j(topic = "API")
public class GetAccountBalanceServlet extends RateLimiterServlet {

  @Autowired
  private Wallet wallet;

  @Autowired
  private WalletOnSpecified walletOnSpecified;

  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      PostParams params = PostParams.getPostParams(request);
      BalanceContract.AccountBalanceRequest.Builder builder
          = BalanceContract.AccountBalanceRequest.newBuilder();
      JsonFormat.merge(params.getParams(), builder, params.isVisible());
      walletOnSpecified.futureGet(statedMayChanged -> fillResponse(params.isVisible(), builder.build(), response), params.getSpecifiedNumber());
    } catch (Exception e) {
      Util.processError(e, response);
    }
  }

  private void fillResponse(boolean visible,
                            BalanceContract.AccountBalanceRequest request,
                            HttpServletResponse response)
      throws Exception {
    BalanceContract.AccountBalanceResponse reply = wallet.getAccountBalance(request);
    if (reply != null) {
      response.getWriter().println(JsonFormat.printToString(reply, visible));
    } else {
      response.getWriter().println("{}");
    }
  }
}
