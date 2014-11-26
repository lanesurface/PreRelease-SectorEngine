#version 120

uniform sampler2D lookup;

varying float veryingHeight;

void main()	{
	gl_FragColor = texture2D(lookup, vec2(veryingHeight, 0.0));
}