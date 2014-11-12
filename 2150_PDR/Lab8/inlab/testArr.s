_Z3fooPi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	eax, DWORD PTR [eax]
	pop	ebp
	ret
_Z4foo2Pi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	eax, DWORD PTR [eax+4]
	pop	ebp
	ret
_Z4foo3Pi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	eax, DWORD PTR [eax+8]
	pop	ebp
	ret
main:
	push	ebp
	mov	ebp, esp
	sub	esp, 20
	mov	DWORD PTR [ebp-12], 1
	mov	DWORD PTR [ebp-8], 2
	mov	DWORD PTR [ebp-4], 3
	lea	eax, [ebp-12]
	mov	DWORD PTR [esp], eax
	call	_Z3fooPi
	lea	eax, [ebp-12]
	mov	DWORD PTR [esp], eax
	call	_Z4foo2Pi
	lea	eax, [ebp-12]
	mov	DWORD PTR [esp], eax
	call	_Z4foo3Pi
	mov	eax, 0
	leave
	ret
