package cn.my.chat.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {
	
	private Long id;
	@NonNull
	private String name;
	@NonNull
	private String password;

}
