_Z3fooRi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	eax, DWORD PTR [eax]
	add	eax, 3
	pop	ebp
	ret
main:
	push	ebp
	mov	ebp, esp
	sub	esp, 20
	mov	DWORD PTR [ebp-4], 4
	lea	eax, [ebp-4]
	mov	DWORD PTR [esp], eax
	call	_Z3fooRi
	mov	eax, 0
	leave
	ret
