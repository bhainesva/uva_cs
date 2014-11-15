_Z18constantArithmeticv:
	mov	eax, 4950
	ret
_Z10redundancyi:
	push	ebx
	sub	esp, 16
	mov	eax, DWORD PTR [esp+24]
	lea	ebx, [eax+eax]
	push	ebx
	push	OFFSET FLAT:_ZSt4cout
	call	_ZNSolsEi
	pop	eax
	pop	edx
	push	ebx
	push	OFFSET FLAT:_ZSt4cout
	call	_ZNSolsEi
	add	esp, 24
	pop	ebx
	ret
_Z14simplificationi:
	xor	eax, eax
	ret
_Z8inliningi:
	mov	eax, DWORD PTR [esp+4]
	add	eax, 1
	ret
main:
	lea	ecx, [esp+4]
	and	esp, -16
	push	DWORD PTR [ecx-4]
	push	ebp
	mov	ebp, esp
	push	ecx
	lea	eax, [ebp-12]
	sub	esp, 28
	push	eax
	push	OFFSET FLAT:_ZSt3cin
	call	_ZNSirsERi
	pop	eax
	push	DWORD PTR [ebp-12]
	call	_Z10redundancyi
	mov	eax, DWORD PTR [ebp-12]
	pop	edx
	pop	ecx
	add	eax, 1
	push	eax
	push	OFFSET FLAT:_ZSt4cout
	call	_ZNSolsEi
	mov	ecx, DWORD PTR [ebp-4]
	add	esp, 16
	xor	eax, eax
	leave
	lea	esp, [ecx-4]
	ret
_GLOBAL__sub_I__Z18constantArithmeticv:
	sub	esp, 24
	push	OFFSET FLAT:_ZStL8__ioinit
	call	_ZNSt8ios_base4InitC1Ev
	add	esp, 12
	push	OFFSET FLAT:__dso_handle
	push	OFFSET FLAT:_ZStL8__ioinit
	push	OFFSET FLAT:_ZNSt8ios_base4InitD1Ev
	call	__cxa_atexit
	add	esp, 28
	ret
