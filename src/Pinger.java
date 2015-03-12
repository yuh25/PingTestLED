import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pinger implements Runnable
{
	public Process p = null;
	private Thread t;
	private String threadName = "Thread";
	public String ip = "";
	public String inputLine;
	public int currentPing = 0;
	public ArrayLoop array = new ArrayLoop(10);
	public int MaxPing = 0;
	public int MinPing = 2147483647;
	public int numPings = 0;

	public Pinger(ArrayLoop array)
	{
		this.array = array;
	}

	public Pinger()
	{
		this.array = new ArrayLoop(1);
	}

	public void run()
	{
		String pingCmd = "ping " + "www.google.com" + " -t";
		try
		{
			try
			{
				Runtime r = Runtime.getRuntime();
				p = r.exec(pingCmd);

				BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
				inputLine = in.readLine();
				while ((inputLine = in.readLine()) != null)
				{

					if (inputLine.equals("Request timed out."))
						System.out.println(ip + ": Request timed out.");
					else
						System.out.println(ip + ": " + +getPing() + "ms");
					array.addInt(currentPing);
					Thread.sleep(50);
				}

				in.close();
			}
			catch (IOException e)
			{
				System.out.println(e);
			}
		}
		catch (InterruptedException e)
		{
			System.out.println("Thread " + threadName + " interrupted.");
		}
	}

	public void setIP(String newIP)
	{
		this.ip = newIP;
	}

	public int getnumPings()
	{
		return numPings;
	}

	public void start()
	{
		if (t == null)
		{
			t = new Thread(this, threadName);
			t.start();
		}
	}

	public void close()
	{
		p.destroy();
	}

	boolean isRunning(Process process)
	{
		try
		{
			process.exitValue();
			return false;
		}
		catch (Exception e)
		{
			return true;
		}
	}

	public int getMax()
	{
		return MaxPing;
	}

	public int getMin()
	{
		return MinPing;
	}

	public int getPing()
	{
		if (inputLine != "" && inputLine != null)
		{
			Pattern Pingfind = Pattern.compile(".*?time=(.*?ms)");
			Matcher c = Pingfind.matcher(inputLine);
			while (c.find())
			{
				String tempstr = c.group(1);
				tempstr = tempstr.replace("ms", "");
				int temp = Integer.parseInt(tempstr);
				currentPing = temp;
				numPings++;
				if (temp > MaxPing)
					MaxPing = temp;
				if (temp < MinPing)
					MinPing = temp;
				return temp;
			}
		}
		return -1;
	}

	public int getCurrentPing()
	{
		return currentPing;
	}

}
