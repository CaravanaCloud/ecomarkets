import sys

def main():
    print("*****************")
    print("* CREATING USER *")
    for i, arg in enumerate(sys.argv):
        print(f"Argument {i}: {arg}")
    print("*****************")

if __name__ == "__main__":
    main()