package org.pbs.sgladapter.adapter;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "sgl",
        url = "${rest.sgl.flashnet.url:http://localhost:8080}",
        path = "/flashnet/api"
)
public interface SGLAdapterClient extends IAdapterClient {
}
