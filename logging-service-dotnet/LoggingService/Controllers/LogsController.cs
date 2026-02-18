using Microsoft.AspNetCore.Mvc;
using LoggerLib;

namespace LoggingService.Controllers
{
    [ApiController]
    [Route("api/logs")]
    public class LogsController : ControllerBase
    {
        [HttpPost]
        [Consumes("application/json")]
        public IActionResult Log([FromBody] LogRequest request)
        {
            FileLogger.CurrentLogger.Log(request.Message);
            return Ok("Log written successfully");
        }
    }

    public class LogRequest
    {
        public string Message { get; set; }
    }
}
