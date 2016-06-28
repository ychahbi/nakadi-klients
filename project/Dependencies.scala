import sbt._

object Dependencies {

	val akkaVersion = "2.4.7"
	val jacksonVersion = "2.7.3"
	val apiDeps = {
		Seq(
		"com.fasterxml.jackson.module" %% "jackson-module-scala"   						% jacksonVersion,
		"com.fasterxml.jackson.core"   % "jackson-annotations"     						% jacksonVersion)
	}

	val clientDeps = {
		Seq(
			"com.typesafe"                 % "config"                  						% "1.3.0",
		  "com.google.guava"             % "guava"                   						% "19.0",
		  "com.fasterxml.jackson.core"   % "jackson-core"           						% jacksonVersion,
		  "com.fasterxml.jackson.module" %% "jackson-module-scala"   						% jacksonVersion,
		  "ch.qos.logback"               % "logback-classic"         						% "1.1.3",
			"com.typesafe.akka"            %% "akka-actor"            	 					% akkaVersion,
			"com.typesafe.akka"            %% "akka-http-experimental" 						% akkaVersion,
			"com.typesafe.akka"            %% "akka-stream" 											% akkaVersion,
			"com.typesafe.akka"            % "akka-slf4j_2.11"                    % "2.4.7",
			"com.typesafe.akka"            %% "akka-testkit"           						% akkaVersion  % "test",
		  "org.scalatest"                %% "scalatest"              						% "2.2.6"      % "test",
		  "com.google.code.findbugs"     % "jsr305"                  						% "1.3.9"      % "test",
		  "junit"                        % "junit"                   						% "4.12"       % "test",
			"org.mockito" 								 % "mockito-core" 											% "1.10.19"    % "test",
			"com.novocode" 								 % "junit-interface"                    % "0.11"       % "test"
		)
	}

	val itDeps = clientDeps ++ {
		Seq(
			"org.zalando.stups"                 % "tokens"                  						% "0.9.9",
			"org.apache.httpcomponents"         % "httpclient"               						% "4.5.2",
			"org.scalatest"                     %% "scalatest"               						% "2.2.6",
			"commons-lang" 											% "commons-lang" 												% "2.6",
			"org.zalando"                       % "jackson-datatype-money"              % "0.6.0"
		)
	}
}
