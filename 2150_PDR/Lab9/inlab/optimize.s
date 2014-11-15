_Z18constantArithmeticv:
	push	ebp
	mov	ebp, esp
	sub	esp, 16
	mov	DWORD PTR [ebp-4], 0
	mov	DWORD PTR [ebp-8], 0
	mov	eax, DWORD PTR [ebp-8]
	add	DWORD PTR [ebp-4], eax
	add	DWORD PTR [ebp-8], 1
	cmp	DWORD PTR [ebp-8], 99
	mov	eax, DWORD PTR [ebp-4]
	leave
	ret
_Z10redundancyi:
	push	ebp
	mov	ebp, esp
	sub	esp, 24
	mov	eax, DWORD PTR [ebp+8]
	add	eax, eax
	mov	DWORD PTR [ebp-12], eax
	mov	eax, DWORD PTR [ebp+8]
	add	eax, eax
	mov	DWORD PTR [ebp-16], eax
	sub	esp, 8
	push	DWORD PTR [ebp-12]
	push	OFFSET FLAT:_ZSt4cout
	call	_ZNSolsEi
	add	esp, 16
	sub	esp, 8
	push	DWORD PTR [ebp-16]
	push	OFFSET FLAT:_ZSt4cout
	call	_ZNSolsEi
	add	esp, 16
	leave
	ret
_Z14simplificationi:
	push	ebp
	mov	ebp, esp
	sub	esp, 16
	mov	eax, DWORD PTR [ebp+8]
	add	eax, eax
	mov	DWORD PTR [ebp-4], eax
	mov	eax, DWORD PTR [ebp+8]
	add	eax, eax
	mov	DWORD PTR [ebp-8], eax
	mov	eax, DWORD PTR [ebp-4]
	sub	eax, DWORD PTR [ebp-8]
	mov	DWORD PTR [ebp-12], eax
	mov	eax, DWORD PTR [ebp-12]
	leave
	ret
_Z8inliningi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	add	eax, 1
	pop	ebp
	ret
main:
	lea	ecx, [esp+4]
	and	esp, -16
	push	DWORD PTR [ecx-4]
	push	ebp
	mov	ebp, esp
	push	ecx
	sub	esp, 36
	sub	esp, 8
	lea	eax, [ebp-28]
	push	eax
	push	OFFSET FLAT:_ZSt3cin
	call	_ZNSirsERi
	add	esp, 16
	call	_Z18constantArithmeticv
	mov	DWORD PTR [ebp-12], eax
	mov	eax, DWORD PTR [ebp-28]
	sub	esp, 12
	push	eax
	call	_Z10redundancyi
	add	esp, 16
	mov	DWORD PTR [ebp-16], eax
	mov	eax, DWORD PTR [ebp-28]
	sub	esp, 12
	push	eax
	call	_Z14simplificationi
	add	esp, 16
	mov	DWORD PTR [ebp-20], eax
	mov	eax, DWORD PTR [ebp-28]
	sub	esp, 12
	push	eax
	call	_Z8inliningi
	add	esp, 16
	mov	DWORD PTR [ebp-24], eax
	sub	esp, 8
	push	DWORD PTR [ebp-24]
	push	OFFSET FLAT:_ZSt4cout
	call	_ZNSolsEi
	add	esp, 16
	mov	eax, 0
	mov	ecx, DWORD PTR [ebp-4]
	leave
	lea	esp, [ecx-4]
	ret
_Z41__static_initialization_and_destruction_0ii:
	push	ebp
	mov	ebp, esp
	sub	esp, 8
	cmp	DWORD PTR [ebp+8], 1
	cmp	DWORD PTR [ebp+12], 65535
	sub	esp, 12
	push	OFFSET FLAT:_ZStL8__ioinit
	call	_ZNSt8ios_base4InitC1Ev
	add	esp, 16
	sub	esp, 4
	push	OFFSET FLAT:__dso_handle
	push	OFFSET FLAT:_ZStL8__ioinit
	push	OFFSET FLAT:_ZNSt8ios_base4InitD1Ev
	call	__cxa_atexit
	add	esp, 16
	leave
	ret
_GLOBAL__sub_I__Z18constantArithmeticv:
	push	ebp
	mov	ebp, esp
	sub	esp, 8
	sub	esp, 8
	push	65535
	push	1
	call	_Z41__static_initialization_and_destruction_0ii
	add	esp, 16
	leave
	ret
