/* Hash table declarations.
   Copyright (C) 2000 Free Software Foundation, Inc.

This file is part of GNU Wget.

GNU Wget is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

GNU Wget is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget; if not, write to the Free Software
Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.  */

/* From XEmacs, and hence from Dragon book. */

#define GOOD_HASH 65599 /* prime number just over 2^16; Dragon book, p. 435 */
#define HASH2(a,b)               (GOOD_HASH * (a)                     + (b))
#define HASH3(a,b,c)             (GOOD_HASH * HASH2 (a,b)             + (c))
#define HASH4(a,b,c,d)           (GOOD_HASH * HASH3 (a,b,c)           + (d))
#define HASH5(a,b,c,d,e)         (GOOD_HASH * HASH4 (a,b,c,d)         + (e))
#define HASH6(a,b,c,d,e,f)       (GOOD_HASH * HASH5 (a,b,c,d,e)       + (f))
#define HASH7(a,b,c,d,e,f,g)     (GOOD_HASH * HASH6 (a,b,c,d,e,f)     + (g))
#define HASH8(a,b,c,d,e,f,g,h)   (GOOD_HASH * HASH7 (a,b,c,d,e,f,g)   + (h))
#define HASH9(a,b,c,d,e,f,g,h,i) (GOOD_HASH * HASH8 (a,b,c,d,e,f,g,h) + (i))

struct hash_table;

struct hash_table *hash_table_new PARAMS ((int,
					   unsigned long (*) (const void *),
					   int (*) (const void *,
						    const void *)));
void hash_table_destroy PARAMS ((struct hash_table *));

void *hash_table_get PARAMS ((struct hash_table *, const void *));
int hash_table_get_pair PARAMS ((struct hash_table *, const void *,
				 void *, void *));
int hash_table_contains PARAMS ((struct hash_table *, const void *));

void hash_table_put PARAMS ((struct hash_table *, const void *, void *));
int hash_table_remove PARAMS ((struct hash_table *, const void *));
void hash_table_clear PARAMS ((struct hash_table *));

void hash_table_map PARAMS ((struct hash_table *,
			     int (*) (void *, void *, void *),
			     void *));
int hash_table_count PARAMS ((struct hash_table *));

unsigned long string_hash PARAMS ((const void *));
int string_cmp PARAMS ((const void *, const void *));
struct hash_table *make_string_hash_table PARAMS ((int));
struct hash_table *make_nocase_string_hash_table PARAMS ((int));
