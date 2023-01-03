package org.pbs.sgladapter.adapter;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
    name = "sgl",
    url = "${rest.sgl.flashnet.url:http://m-mtsc0ap-lab.hq.corp.pbs.org:11000}",
    path = "/flashnet/api"
)
public interface SGLAdapterClient extends AdapterClient {
}
