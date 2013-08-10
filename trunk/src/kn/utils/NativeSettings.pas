library jnitest;

uses
  jni, sysutils, classes;

type
  HList = Integer;
  
procedure OpenFile(List: TStringList; FileName: string);
begin
  if not FileExists(FileName) or (List = nil) then
    exit;
  try
    List.LoadFromFile(FileName);
  except
  end;
end;
  
function Java_kn_utils_NativeSettings_openSettings(PEnv: PJNIEnv ; Obj: integer; filePath: JString): HList; cdecl ;
var
  List: TStringList;
  isCopy: byte;
  c: PChar;
begin
  List:= TStringList.Create;
  Result:= Integer(List);
  c:= PEnv^.GetStringUTFChars(PEnv, filePath, isCopy);
  try
     OpenFile(List, c);
  finally
    if isCopy > 0 then
      PEnv^.ReleaseStringUTFChars(PEnv, filePath, c);
  end;
end;

function Java_kn_utils_NativeSettings_getValue(PEnv: PJNIEnv; Obj: integer; list: HList; name: JString): JString; cdecl;
var
   SL: TStringList;
   c: PChar; 
   s: string;
   isCopy: byte;
begin
  SL:= TObject(list) as TStringList;
  s:= '';
  c:= PEnv^.GetStringUTFChars(PEnv, name, isCopy);
  try
     if SL <> nil then
       s:= SL.Values[c];
  finally
    if isCopy > 0 then
      PEnv^.ReleaseStringUTFChars(PEnv, name, c);
  end;    
  Result := PEnv^.NewStringUTF(PEnv, PChar(s));
end;

function Java_kn_utils_NativeSettings_setValue( PEnv: PJNIEnv ; Obj: integer; list: HList; name, value: JString): integer;cdecl ;
var
  SL: TStringList;
  c1, c2: PChar;
  isCopy1, isCopy2: byte;
begin
  Result:= 1;
  SL:= TObject(list) as TStringList;
  c1:= PEnv^.GetStringUTFChars(PEnv, name, isCopy1);
  c2:= PEnv^.GetStringUTFChars(PEnv, value, isCopy2);
  try
     if SL <> nil then
       SL.Values[c1]:= c2;
  finally
    if isCopy1 > 0 then
      PEnv^.ReleaseStringUTFChars(PEnv, name, c1);
    if isCopy2 > 0 then
      PEnv^.ReleaseStringUTFChars(PEnv, value, c2);      
  end;    
  Result:= 0;
end;

function Java_kn_utils_NativeSettings_release ( PEnv: PJNIEnv ; Obj: integer; list: HList; filePath: JString): integer;cdecl;
var
  SL: TStringList;
  c: PChar;
  isCopy: byte;
begin
  Result:= 0;
  c:= PEnv^.GetStringUTFChars(PEnv, filePath, isCopy);
  try
    try
      SL:= TObject(list) as TStringList;
      SL.SaveToFile(c);
      SL.Free;
    except
      Result:= 1;
    end;
  finally
     if isCopy > 0 then
       PEnv^.ReleaseStringUTFChars(PEnv, filePath, c);
  end;
end;

exports
  Java_kn_utils_NativeSettings_openSettings ,
  Java_kn_utils_NativeSettings_getValue ,
  Java_kn_utils_NativeSettings_setValue ,
  Java_kn_utils_NativeSettings_release;
begin
end.
