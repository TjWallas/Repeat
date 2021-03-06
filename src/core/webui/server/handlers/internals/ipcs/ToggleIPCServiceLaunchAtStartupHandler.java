package core.webui.server.handlers.internals.ipcs;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.nio.protocol.HttpAsyncExchange;
import org.apache.http.protocol.HttpContext;

import core.ipc.IIPCService;
import core.webcommon.HttpServerUtilities;
import core.webui.server.handlers.AbstractSingleMethodHttpHandler;
import core.webui.server.handlers.AbstractUIHttpHandler;
import core.webui.server.handlers.CommonTask;
import core.webui.server.handlers.renderedobjects.ObjectRenderer;

public class ToggleIPCServiceLaunchAtStartupHandler extends AbstractUIHttpHandler {

	public ToggleIPCServiceLaunchAtStartupHandler(ObjectRenderer objectRenderer) {
		super(objectRenderer, AbstractSingleMethodHttpHandler.POST_METHOD);
	}

	@Override
	protected Void handleAllowedRequestWithBackend(HttpRequest request, HttpAsyncExchange exchange, HttpContext context)
			throws HttpException, IOException {
		Map<String, String> params = HttpServerUtilities.parseSimplePostParameters(request);
		if (params == null) {
			return HttpServerUtilities.prepareHttpResponse(exchange, 500, "Unable to get POST paramters.");
		}

		IIPCService service = CommonTask.getIPCService(params);
		if (service == null) {
			return HttpServerUtilities.prepareHttpResponse(exchange, 500, "Unable to get IPC service.");
		}

		service.setLaunchAtStartup(!service.isLaunchAtStartup());
		return renderedIpcServices(exchange);
	}
}
