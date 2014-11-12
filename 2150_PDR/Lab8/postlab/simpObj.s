_ZN4Test4setXEi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	edx, DWORD PTR [ebp+12]
	mov	DWORD PTR [eax], edx
	pop	ebp
	ret
main:
	push	ebp
	mov	ebp, esp
	sub	esp, 32
	mov	DWORD PTR [ebp-20], 0
	mov	BYTE PTR [ebp-16], 0
	mov	BYTE PTR [ebp-15], 0
	mov	DWORD PTR [ebp-12], 0
	mov	DWORD PTR [ebp-8], 0
	mov	DWORD PTR [ebp-4], 0
	push	3
	lea	eax, [ebp-20]
	push	eax
	call	_ZN4Test4setXEi
	add	esp, 8
	mov	DWORD PTR [ebp-4], 2
	mov	eax, 0
	leave
	ret
