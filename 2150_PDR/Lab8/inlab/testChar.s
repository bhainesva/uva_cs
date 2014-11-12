_Z3fooc:
	push	ebp
	mov	ebp, esp
	sub	esp, 4
	mov	eax, DWORD PTR [ebp+8]
	mov	BYTE PTR [ebp-4], al
	movsx	eax, BYTE PTR [ebp-4]
	add	eax, 3
	leave
	ret
main:
	push	ebp
	mov	ebp, esp
	push	97
	call	_Z3fooc
	add	esp, 4
	mov	eax, 0
	leave
	ret
