_Z3fooi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	add	eax, 3
	pop	ebp
	ret
main:
	push	ebp
	mov	ebp, esp
	push	4
	call	_Z3fooi
	add	esp, 4
	mov	eax, 0
	leave
	ret
