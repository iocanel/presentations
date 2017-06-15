#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * Converts aruments from String to Double, evaluation expressions (e.g. sum(1,1) to 2).
 * Created by iocanel on 6/12/17.
 */
@Service
public class Evaluator {

	private final RestTemplate template;

	static final String NAME = "name";
	static final String ARGUMENTS = "arguments";

	static final String CALL_REGEX = "(?<name>[a-z]+)${symbol_escape}${symbol_escape}((?<arguments>[a-z0-9,.()]+)${symbol_escape}${symbol_escape})";
	static final Pattern CALL_PATTERN = Pattern.compile(CALL_REGEX);

	public Evaluator(RestTemplate template) {
		this.template = template;
	}

	public Double eval(String s) {
		Matcher m = CALL_PATTERN.matcher(s);
		if (m.matches()) {
			String name = m.group(NAME);
			List<String> arguments = parseArguments(m.group(ARGUMENTS));
			return template.getForEntity("http://" + name + "/eval/" + arguments.stream().collect(Collectors.joining("/")), Double.class).getBody();
		}
		return Double.parseDouble(s.trim());
	}

	public static List<String> parseArguments(String str) {
		if (str == null) {
			return Collections.emptyList();
		}

		int depth = 0;
		List<String> arguments = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ' ') {
				continue;
			}

			if (str.charAt(i) == '(') {
				sb.append(str.charAt(i));
				depth++;
			} else if (str.charAt(i) == ')') {
				sb.append(str.charAt(i));
				depth--;
			} else if (str.charAt(i) != ',' || depth != 0) {
				sb.append(str.charAt(i));
			} else {
				arguments.add(sb.toString());
				sb = new StringBuilder();
			}
		}
		if (sb.length() > 0) {
			arguments.add(sb.toString());
		}
		return arguments;
	}    
}
