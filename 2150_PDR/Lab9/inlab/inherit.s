_ZN6Parent4setXEi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	edx, DWORD PTR [ebp+12]
	mov	DWORD PTR [eax], edx
	pop	ebp
	ret
_ZN6Parent4setYEi:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	edx, DWORD PTR [ebp+12]
	mov	DWORD PTR [eax+4], edx
	pop	ebp
	ret
_ZN5Child6xPlusYEv:
	push	ebp
	mov	ebp, esp
	mov	eax, DWORD PTR [ebp+8]
	mov	edx, DWORD PTR [eax]
	mov	eax, DWORD PTR [ebp+8]
	mov	eax, DWORD PTR [eax+4]
	add	eax, edx
	pop	ebp
	ret
_Z5scopev:
	push	ebp
	mov	ebp, esp
	sub	esp, 24
	sub	esp, 12
	push	12
	call	_Znwj
	add	esp, 16
	mov	DWORD PTR [eax], 0
	mov	DWORD PTR [eax+4], 0
	mov	DWORD PTR [eax+8], 0
	mov	DWORD PTR [ebp-12], eax
	mov	eax, DWORD PTR [ebp-12]
	sub	esp, 8
	push	2
	push	eax
	call	_ZN6Parent4setXEi
	add	esp, 16
	mov	eax, DWORD PTR [ebp-12]
	sub	esp, 8
	push	3
	push	eax
	call	_ZN6Parent4setYEi
	add	esp, 16
	sub	esp, 12
	push	DWORD PTR [ebp-12]
	call	_ZN5Child6xPlusYEv
	add	esp, 16
	mov	DWORD PTR [ebp-16], eax
	leave
	ret
main:
	lea	ecx, [esp+4]
	and	esp, -16
	push	DWORD PTR [ecx-4]
	push	ebp
	mov	ebp, esp
	push	ecx
	sub	esp, 4
	call	_Z5scopev
	mov	eax, 0
	add	esp, 4
	pop	ecx
	pop	ebp
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
_GLOBAL__sub_I__Z5scopev:
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
