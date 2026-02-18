using System;
using System.IO;

namespace LoggerLib
{
    public class FileLogger
    {
        private static readonly FileLogger logger = new FileLogger();

        private FileLogger() { }

        public static FileLogger CurrentLogger => logger;

        public void Log(string message)
        {
            string filePath = Path.Combine(
                AppDomain.CurrentDomain.BaseDirectory,
                "logs",
                "app.log"
            );

            // Ensure directory exists
            Directory.CreateDirectory(Path.GetDirectoryName(filePath)!);

            string messageData =
                $"Logged at {DateTime.Now:yyyy-MM-dd HH:mm:ss} - {message}";

            // Safe write (auto close)
            File.AppendAllText(filePath, messageData + Environment.NewLine);
        }
    }
}
