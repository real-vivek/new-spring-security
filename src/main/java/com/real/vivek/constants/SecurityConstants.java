package com.real.vivek.constants;

public class SecurityConstants {
	
	//This value will be know to back end application only
	//Here we have this in constants class but in production we have to ask devops team to inject this value at runtime as runtime variable using Jenkins
	//Also we can configure this environment variable in prod server
	public static final String JWT_KEY="someSecretKeyWhichIsMoreThan256Bits";
	public static final String JWT_HEADER = "Authorization";

}
